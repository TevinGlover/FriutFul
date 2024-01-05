import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Balance from './balance';
import BalanceDetail from './balance-detail';
import BalanceUpdate from './balance-update';
import BalanceDeleteDialog from './balance-delete-dialog';

const BalanceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Balance />} />
    <Route path="new" element={<BalanceUpdate />} />
    <Route path=":id">
      <Route index element={<BalanceDetail />} />
      <Route path="edit" element={<BalanceUpdate />} />
      <Route path="delete" element={<BalanceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BalanceRoutes;
