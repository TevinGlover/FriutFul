import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './child.reducer';

export const ChildDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const childEntity = useAppSelector(state => state.child.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="childDetailsHeading">Child</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{childEntity.id}</dd>
          <dt>
            <span id="userFristName">User Frist Name</span>
          </dt>
          <dd>{childEntity.userFristName}</dd>
          <dt>
            <span id="userLastName">User Last Name</span>
          </dt>
          <dd>{childEntity.userLastName}</dd>
          <dt>Credit Score</dt>
          <dd>{childEntity.creditScore ? childEntity.creditScore.id : ''}</dd>
          <dt>Points</dt>
          <dd>{childEntity.points ? childEntity.points.id : ''}</dd>
          <dt>Account</dt>
          <dd>{childEntity.account ? childEntity.account.id : ''}</dd>
          <dt>Task</dt>
          <dd>{childEntity.task ? childEntity.task.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/child" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/child/${childEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChildDetail;
