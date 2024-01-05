import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './credit-score.reducer';

export const CreditScoreDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const creditScoreEntity = useAppSelector(state => state.creditScore.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="creditScoreDetailsHeading">Credit Score</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{creditScoreEntity.id}</dd>
          <dt>
            <span id="month">Month</span>
          </dt>
          <dd>{creditScoreEntity.month ? <TextFormat value={creditScoreEntity.month} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="scoreNumber">Score Number</span>
          </dt>
          <dd>{creditScoreEntity.scoreNumber}</dd>
        </dl>
        <Button tag={Link} to="/credit-score" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/credit-score/${creditScoreEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CreditScoreDetail;
