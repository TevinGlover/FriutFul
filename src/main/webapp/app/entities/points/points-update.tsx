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
import { IPoints } from 'app/shared/model/points.model';
import { getEntity, updateEntity, createEntity, reset } from './points.reducer';

export const PointsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const parents = useAppSelector(state => state.parents.entities);
  const children = useAppSelector(state => state.child.entities);
  const pointsEntity = useAppSelector(state => state.points.entity);
  const loading = useAppSelector(state => state.points.loading);
  const updating = useAppSelector(state => state.points.updating);
  const updateSuccess = useAppSelector(state => state.points.updateSuccess);

  const handleClose = () => {
    navigate('/points');
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    if (values.used !== undefined && typeof values.used !== 'number') {
      values.used = Number(values.used);
    }

    const entity = {
      ...pointsEntity,
      ...values,
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
          ...pointsEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fruitFulApp.points.home.createOrEditLabel" data-cy="PointsCreateUpdateHeading">
            Create or edit a Points
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="points-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Amount" id="points-amount" name="amount" data-cy="amount" type="text" />
              <ValidatedField label="Used" id="points-used" name="used" data-cy="used" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/points" replace color="info">
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

export default PointsUpdate;
