import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './parents.reducer';

export const Parents = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const parentsList = useAppSelector(state => state.parents.entities);
  const loading = useAppSelector(state => state.parents.loading);

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
      <h2 id="parents-heading" data-cy="ParentsHeading">
        Parents
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/parents/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Parents
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {parentsList && parentsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('parentsFristName')}>
                  Parents Frist Name <FontAwesomeIcon icon={getSortIconByFieldName('parentsFristName')} />
                </th>
                <th className="hand" onClick={sort('parentsLastName')}>
                  Parents Last Name <FontAwesomeIcon icon={getSortIconByFieldName('parentsLastName')} />
                </th>
                <th>
                  Credit Score <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Points <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Task <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Child <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {parentsList.map((parents, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/parents/${parents.id}`} color="link" size="sm">
                      {parents.id}
                    </Button>
                  </td>
                  <td>{parents.parentsFristName}</td>
                  <td>{parents.parentsLastName}</td>
                  <td>{parents.creditScore ? <Link to={`/credit-score/${parents.creditScore.id}`}>{parents.creditScore.id}</Link> : ''}</td>
                  <td>{parents.points ? <Link to={`/points/${parents.points.id}`}>{parents.points.id}</Link> : ''}</td>
                  <td>{parents.task ? <Link to={`/task/${parents.task.id}`}>{parents.task.id}</Link> : ''}</td>
                  <td>
                    {parents.children
                      ? parents.children.map((val, j) => (
                          <span key={j}>
                            <Link to={`/child/${val.id}`}>{val.id}</Link>
                            {j === parents.children.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/parents/${parents.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/parents/${parents.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/parents/${parents.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Parents found</div>
        )}
      </div>
    </div>
  );
};

export default Parents;
