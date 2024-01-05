import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICreditScore } from 'app/shared/model/credit-score.model';
import { getEntities as getCreditScores } from 'app/entities/credit-score/credit-score.reducer';
import { IPoints } from 'app/shared/model/points.model';
import { getEntities as getPoints } from 'app/entities/points/points.reducer';
import { IBalance } from 'app/shared/model/balance.model';
import { getEntities as getBalances } from 'app/entities/balance/balance.reducer';
import { ITask } from 'app/shared/model/task.model';
import { getEntities as getTasks } from 'app/entities/task/task.reducer';
import { IParents } from 'app/shared/model/parents.model';
import { getEntities as getParents } from 'app/entities/parents/parents.reducer';
import { IChild } from 'app/shared/model/child.model';
import { getEntity, updateEntity, createEntity, reset } from './child.reducer';

export const ChildUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const creditScores = useAppSelector(state => state.creditScore.entities);
  const points = useAppSelector(state => state.points.entities);
  const balances = useAppSelector(state => state.balance.entities);
  const tasks = useAppSelector(state => state.task.entities);
  const parents = useAppSelector(state => state.parents.entities);
  const childEntity = useAppSelector(state => state.child.entity);
  const loading = useAppSelector(state => state.child.loading);
  const updating = useAppSelector(state => state.child.updating);
  const updateSuccess = useAppSelector(state => state.child.updateSuccess);

  const handleClose = () => {
    navigate('/child');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCreditScores({}));
    dispatch(getPoints({}));
    dispatch(getBalances({}));
    dispatch(getTasks({}));
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

    const entity = {
      ...childEntity,
      ...values,
      creditScore: creditScores.find(it => it.id.toString() === values.creditScore.toString()),
      points: points.find(it => it.id.toString() === values.points.toString()),
      account: balances.find(it => it.id.toString() === values.account.toString()),
      task: tasks.find(it => it.id.toString() === values.task.toString()),
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
          ...childEntity,
          creditScore: childEntity?.creditScore?.id,
          points: childEntity?.points?.id,
          account: childEntity?.account?.id,
          task: childEntity?.task?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fruitFulApp.child.home.createOrEditLabel" data-cy="ChildCreateUpdateHeading">
            Create or edit a Child
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="child-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="User Frist Name" id="child-userFristName" name="userFristName" data-cy="userFristName" type="text" />
              <ValidatedField label="User Last Name" id="child-userLastName" name="userLastName" data-cy="userLastName" type="text" />
              <ValidatedField id="child-creditScore" name="creditScore" data-cy="creditScore" label="Credit Score" type="select">
                <option value="" key="0" />
                {creditScores
                  ? creditScores.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="child-points" name="points" data-cy="points" label="Points" type="select">
                <option value="" key="0" />
                {points
                  ? points.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="child-account" name="account" data-cy="account" label="Account" type="select">
                <option value="" key="0" />
                {balances
                  ? balances.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="child-task" name="task" data-cy="task" label="Task" type="select">
                <option value="" key="0" />
                {tasks
                  ? tasks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/child" replace color="info">
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

export default ChildUpdate;
