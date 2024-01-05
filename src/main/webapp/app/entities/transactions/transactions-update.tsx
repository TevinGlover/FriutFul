import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBalance } from 'app/shared/model/balance.model';
import { getEntities as getBalances } from 'app/entities/balance/balance.reducer';
import { ITransactions } from 'app/shared/model/transactions.model';
import { getEntity, updateEntity, createEntity, reset } from './transactions.reducer';

export const TransactionsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const balances = useAppSelector(state => state.balance.entities);
  const transactionsEntity = useAppSelector(state => state.transactions.entity);
  const loading = useAppSelector(state => state.transactions.loading);
  const updating = useAppSelector(state => state.transactions.updating);
  const updateSuccess = useAppSelector(state => state.transactions.updateSuccess);

  const handleClose = () => {
    navigate('/transactions');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBalances({}));
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...transactionsEntity,
      ...values,
      account: balances.find(it => it.id.toString() === values.account.toString()),
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
          date: displayDefaultDateTime(),
        }
      : {
          ...transactionsEntity,
          date: convertDateTimeFromServer(transactionsEntity.date),
          account: transactionsEntity?.account?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fruitFulApp.transactions.home.createOrEditLabel" data-cy="TransactionsCreateUpdateHeading">
            Create or edit a Transactions
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="transactions-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Amount" id="transactions-amount" name="amount" data-cy="amount" type="text" />
              <ValidatedField
                label="Date"
                id="transactions-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="transactions-account" name="account" data-cy="account" label="Account" type="select">
                <option value="" key="0" />
                {balances
                  ? balances.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/transactions" replace color="info">
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

export default TransactionsUpdate;
