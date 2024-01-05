import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './points.reducer';

export const PointsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pointsEntity = useAppSelector(state => state.points.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pointsDetailsHeading">Points</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{pointsEntity.id}</dd>
          <dt>
            <span id="amount">Amount</span>
          </dt>
          <dd>{pointsEntity.amount}</dd>
          <dt>
            <span id="used">Used</span>
          </dt>
          <dd>{pointsEntity.used}</dd>
        </dl>
        <Button tag={Link} to="/points" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/points/${pointsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PointsDetail;
