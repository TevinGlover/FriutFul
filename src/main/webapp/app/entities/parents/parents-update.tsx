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
import { ITask } from 'app/shared/model/task.model';
import { getEntities as getTasks } from 'app/entities/task/task.reducer';
import { IChild } from 'app/shared/model/child.model';
import { getEntities as getChildren } from 'app/entities/child/child.reducer';
import { IParents } from 'app/shared/model/parents.model';
import { getEntity, updateEntity, createEntity, reset } from './parents.reducer';

export const ParentsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const creditScores = useAppSelector(state => state.creditScore.entities);
  const points = useAppSelector(state => state.points.entities);
  const tasks = useAppSelector(state => state.task.entities);
  const children = useAppSelector(state => state.child.entities);
  const parentsEntity = useAppSelector(state => state.parents.entity);
  const loading = useAppSelector(state => state.parents.loading);
  const updating = useAppSelector(state => state.parents.updating);
  const updateSuccess = useAppSelector(state => state.parents.updateSuccess);

  const handleClose = () => {
    navigate('/parents');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCreditScores({}));
    dispatch(getPoints({}));
    dispatch(getTasks({}));
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

    const entity = {
      ...parentsEntity,
      ...values,
      children: mapIdList(values.children),
      creditScore: creditScores.find(it => it.id.toString() === values.creditScore.toString()),
      points: points.find(it => it.id.toString() === values.points.toString()),
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
          ...parentsEntity,
          creditScore: parentsEntity?.creditScore?.id,
          points: parentsEntity?.points?.id,
          task: parentsEntity?.task?.id,
          children: parentsEntity?.children?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fruitFulApp.parents.home.createOrEditLabel" data-cy="ParentsCreateUpdateHeading">
            Create or edit a Parents
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="parents-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Parents Frist Name"
                id="parents-parentsFristName"
                name="parentsFristName"
                data-cy="parentsFristName"
                type="text"
              />
              <ValidatedField
                label="Parents Last Name"
                id="parents-parentsLastName"
                name="parentsLastName"
                data-cy="parentsLastName"
                type="text"
              />
              <ValidatedField id="parents-creditScore" name="creditScore" data-cy="creditScore" label="Credit Score" type="select">
                <option value="" key="0" />
                {creditScores
                  ? creditScores.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="parents-points" name="points" data-cy="points" label="Points" type="select">
                <option value="" key="0" />
                {points
                  ? points.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="parents-task" name="task" data-cy="task" label="Task" type="select">
                <option value="" key="0" />
                {tasks
                  ? tasks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Child" id="parents-child" data-cy="child" type="select" multiple name="children">
                <option value="" key="0" />
                {children
                  ? children.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/parents" replace color="info">
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

export default ParentsUpdate;
