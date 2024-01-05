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
import { ICreditScore } from 'app/shared/model/credit-score.model';
import { getEntity, updateEntity, createEntity, reset } from './credit-score.reducer';

export const CreditScoreUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const parents = useAppSelector(state => state.parents.entities);
  const children = useAppSelector(state => state.child.entities);
  const creditScoreEntity = useAppSelector(state => state.creditScore.entity);
  const loading = useAppSelector(state => state.creditScore.loading);
  const updating = useAppSelector(state => state.creditScore.updating);
  const updateSuccess = useAppSelector(state => state.creditScore.updateSuccess);

  const handleClose = () => {
    navigate('/credit-score');
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
    values.month = convertDateTimeToServer(values.month);
    if (values.scoreNumber !== undefined && typeof values.scoreNumber !== 'number') {
      values.scoreNumber = Number(values.scoreNumber);
    }

    const entity = {
      ...creditScoreEntity,
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
      ? {
          month: displayDefaultDateTime(),
        }
      : {
          ...creditScoreEntity,
          month: convertDateTimeFromServer(creditScoreEntity.month),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fruitFulApp.creditScore.home.createOrEditLabel" data-cy="CreditScoreCreateUpdateHeading">
            Create or edit a Credit Score
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="credit-score-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Month"
                id="credit-score-month"
                name="month"
                data-cy="month"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Score Number" id="credit-score-scoreNumber" name="scoreNumber" data-cy="scoreNumber" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/credit-score" replace color="info">
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

export default CreditScoreUpdate;
