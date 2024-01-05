import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './achivement.reducer';

export const AchivementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const achivementEntity = useAppSelector(state => state.achivement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="achivementDetailsHeading">Achivement</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{achivementEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{achivementEntity.name}</dd>
          <dt>
            <span id="pointValue">Point Value</span>
          </dt>
          <dd>{achivementEntity.pointValue}</dd>
          <dt>Child</dt>
          <dd>{achivementEntity.child ? achivementEntity.child.id : ''}</dd>
          <dt>Parents</dt>
          <dd>{achivementEntity.parents ? achivementEntity.parents.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/achivement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/achivement/${achivementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AchivementDetail;
