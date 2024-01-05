import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IParents } from 'app/shared/model/parents.model';
import { getEntities as getParents } from 'app/entities/parents/parents.reducer';
import { IChild } from 'app/shared/model/child.model';
import { getEntities as getChildren } from 'app/entities/child/child.reducer';
import { IBalance } from 'app/shared/model/balance.model';
import { getEntity, updateEntity, createEntity, reset } from './balance.reducer';

export const BalanceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const parents = useAppSelector(state => state.parents.entities);
  const children = useAppSelector(state => state.child.entities);
  const balanceEntity = useAppSelector(state => state.balance.entity);
  const loading = useAppSelector(state => state.balance.loading);
  const updating = useAppSelector(state => state.balance.updating);
  const updateSuccess = useAppSelector(state => state.balance.updateSuccess);

  const handleClose = () => {
    navigate('/balance');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getParents({}));
    dispatch(getChildren({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.creditcardNum !== undefined && typeof values.creditcardNum !== 'number') {
      values.creditcardNum = Number(values.creditcardNum);
    }
    values.vaildThru = convertDateTimeToServer(values.vaildThru);
    if (values.cvs !== undefined && typeof values.cvs !== 'number') {
      values.cvs = Number(values.cvs);
    }
    if (values.maxLimit !== undefined && typeof values.maxLimit !== 'number') {
      values.maxLimit = Number(values.maxLimit);
    }
    if (values.thrityPrecentLimit !== undefined && typeof values.thrityPrecentLimit !== 'number') {
      values.thrityPrecentLimit = Number(values.thrityPrecentLimit);
    }

    const entity = {
      ...balanceEntity,
      ...values,
      parents: parents.find(it => it.id.toString() === values.parents.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          vaildThru: displayDefaultDateTime(),
        }
      : {
          ...balanceEntity,
          vaildThru: convertDateTimeFromServer(balanceEntity.vaildThru),
          parents: balanceEntity?.parents?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fruitFulApp.balance.home.createOrEditLabel" data-cy="BalanceCreateUpdateHeading">
            Create or edit a Balance
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="balance-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Credit Card Type"
                id="balance-creditCardType"
                name="creditCardType"
                data-cy="creditCardType"
                type="text"
              />
              <ValidatedField label="Creditcard Num" id="balance-creditcardNum" name="creditcardNum" data-cy="creditcardNum" type="text" />
              <ValidatedField
                label="Vaild Thru"
                id="balance-vaildThru"
                name="vaildThru"
                data-cy="vaildThru"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Cvs" id="balance-cvs" name="cvs" data-cy="cvs" type="text" />
              <ValidatedField label="Max Limit" id="balance-maxLimit" name="maxLimit" data-cy="maxLimit" type="text" />
              <ValidatedField
                label="Thrity Precent Limit"
                id="balance-thrityPrecentLimit"
                name="thrityPrecentLimit"
                data-cy="thrityPrecentLimit"
                type="text"
              />
              <ValidatedField id="balance-parents" name="parents" data-cy="parents" label="Parents" type="select">
                <option value="" key="0" />
                {parents
                  ? parents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/balance" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BalanceUpdate;
