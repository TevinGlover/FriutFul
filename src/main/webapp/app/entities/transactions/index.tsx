import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Transactions from './transactions';
import TransactionsDetail from './transactions-detail';
import TransactionsUpdate from './transactions-update';
import TransactionsDeleteDialog from './transactions-delete-dialog';

const TransactionsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Transactions />} />
    <Route path="new" element={<TransactionsUpdate />} />
    <Route path=":id">
      <Route index element={<TransactionsDetail />} />
      <Route path="edit" element={<TransactionsUpdate />} />
      <Route path="delete" element={<TransactionsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TransactionsRoutes;
