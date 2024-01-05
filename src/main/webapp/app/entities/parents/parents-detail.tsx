import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './parents.reducer';

export const ParentsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const parentsEntity = useAppSelector(state => state.parents.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="parentsDetailsHeading">Parents</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{parentsEntity.id}</dd>
          <dt>
            <span id="parentsFristName">Parents Frist Name</span>
          </dt>
          <dd>{parentsEntity.parentsFristName}</dd>
          <dt>
            <span id="parentsLastName">Parents Last Name</span>
          </dt>
          <dd>{parentsEntity.parentsLastName}</dd>
          <dt>Credit Score</dt>
          <dd>{parentsEntity.creditScore ? parentsEntity.creditScore.id : ''}</dd>
          <dt>Points</dt>
          <dd>{parentsEntity.points ? parentsEntity.points.id : ''}</dd>
          <dt>Task</dt>
          <dd>{parentsEntity.task ? parentsEntity.task.id : ''}</dd>
          <dt>Child</dt>
          <dd>
            {parentsEntity.children
              ? parentsEntity.children.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {parentsEntity.children && i === parentsEntity.children.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/parents" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/parents/${parentsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ParentsDetail;
