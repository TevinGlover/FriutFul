import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './balance.reducer';

export const BalanceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const balanceEntity = useAppSelector(state => state.balance.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="balanceDetailsHeading">Balance</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{balanceEntity.id}</dd>
          <dt>
            <span id="creditCardType">Credit Card Type</span>
          </dt>
          <dd>{balanceEntity.creditCardType}</dd>
          <dt>
            <span id="creditcardNum">Creditcard Num</span>
          </dt>
          <dd>{balanceEntity.creditcardNum}</dd>
          <dt>
            <span id="vaildThru">Vaild Thru</span>
          </dt>
          <dd>{balanceEntity.vaildThru ? <TextFormat value={balanceEntity.vaildThru} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="cvs">Cvs</span>
          </dt>
          <dd>{balanceEntity.cvs}</dd>
          <dt>
            <span id="maxLimit">Max Limit</span>
          </dt>
          <dd>{balanceEntity.maxLimit}</dd>
          <dt>
            <span id="thrityPrecentLimit">Thrity Precent Limit</span>
          </dt>
          <dd>{balanceEntity.thrityPrecentLimit}</dd>
          <dt>Parents</dt>
          <dd>{balanceEntity.parents ? balanceEntity.parents.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/balance" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/balance/${balanceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BalanceDetail;
