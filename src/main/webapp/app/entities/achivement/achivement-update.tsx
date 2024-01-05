import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IChild } from 'app/shared/model/child.model';
import { getEntities as getChildren } from 'app/entities/child/child.reducer';
import { IParents } from 'app/shared/model/parents.model';
import { getEntities as getParents } from 'app/entities/parents/parents.reducer';
import { IAchivement } from 'app/shared/model/achivement.model';
import { getEntity, updateEntity, createEntity, reset } from './achivement.reducer';

export const AchivementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const children = useAppSelector(state => state.child.entities);
  const parents = useAppSelector(state => state.parents.entities);
  const achivementEntity = useAppSelector(state => state.achivement.entity);
  const loading = useAppSelector(state => state.achivement.loading);
  const updating = useAppSelector(state => state.achivement.updating);
  const updateSuccess = useAppSelector(state => state.achivement.updateSuccess);

  const handleClose = () => {
    navigate('/achivement');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getChildren({}));
    dispatch(getParents({}));
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
    if (values.pointValue !== undefined && typeof values.pointValue !== 'number') {
      values.pointValue = Number(values.pointValue);
    }

    const entity = {
      ...achivementEntity,
      ...values,
      child: children.find(it => it.id.toString() === values.child.toString()),
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
      ? {}
      : {
          ...achivementEntity,
          child: achivementEntity?.child?.id,
          parents: achivementEntity?.parents?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fruitFulApp.achivement.home.createOrEditLabel" data-cy="AchivementCreateUpdateHeading">
            Create or edit a Achivement
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="achivement-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="achivement-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Point Value" id="achivement-pointValue" name="pointValue" data-cy="pointValue" type="text" />
              <ValidatedField id="achivement-child" name="child" data-cy="child" label="Child" type="select">
                <option value="" key="0" />
                {children
                  ? children.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="achivement-parents" name="parents" data-cy="parents" label="Parents" type="select">
                <option value="" key="0" />
                {parents
                  ? parents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/achivement" replace color="info">
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

export default AchivementUpdate;
