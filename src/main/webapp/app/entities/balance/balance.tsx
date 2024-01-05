import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './balance.reducer';

export const Balance = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const balanceList = useAppSelector(state => state.balance.entities);
  const loading = useAppSelector(state => state.balance.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="balance-heading" data-cy="BalanceHeading">
        Balances
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/balance/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Balance
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {balanceList && balanceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('creditCardType')}>
                  Credit Card Type <FontAwesomeIcon icon={getSortIconByFieldName('creditCardType')} />
                </th>
                <th className="hand" onClick={sort('creditcardNum')}>
                  Creditcard Num <FontAwesomeIcon icon={getSortIconByFieldName('creditcardNum')} />
                </th>
                <th className="hand" onClick={sort('vaildThru')}>
                  Vaild Thru <FontAwesomeIcon icon={getSortIconByFieldName('vaildThru')} />
                </th>
                <th className="hand" onClick={sort('cvs')}>
                  Cvs <FontAwesomeIcon icon={getSortIconByFieldName('cvs')} />
                </th>
                <th className="hand" onClick={sort('maxLimit')}>
                  Max Limit <FontAwesomeIcon icon={getSortIconByFieldName('maxLimit')} />
                </th>
                <th className="hand" onClick={sort('thrityPrecentLimit')}>
                  Thrity Precent Limit <FontAwesomeIcon icon={getSortIconByFieldName('thrityPrecentLimit')} />
                </th>
                <th>
                  Parents <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {balanceList.map((balance, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/balance/${balance.id}`} color="link" size="sm">
                      {balance.id}
                    </Button>
                  </td>
                  <td>{balance.creditCardType}</td>
                  <td>{balance.creditcardNum}</td>
                  <td>{balance.vaildThru ? <TextFormat type="date" value={balance.vaildThru} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{balance.cvs}</td>
                  <td>{balance.maxLimit}</td>
                  <td>{balance.thrityPrecentLimit}</td>
                  <td>{balance.parents ? <Link to={`/parents/${balance.parents.id}`}>{balance.parents.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/balance/${balance.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/balance/${balance.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/balance/${balance.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Balances found</div>
        )}
      </div>
    </div>
  );
};

export default Balance;
