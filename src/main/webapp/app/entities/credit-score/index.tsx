import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CreditScore from './credit-score';
import CreditScoreDetail from './credit-score-detail';
import CreditScoreUpdate from './credit-score-update';
import CreditScoreDeleteDialog from './credit-score-delete-dialog';

const CreditScoreRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CreditScore />} />
    <Route path="new" element={<CreditScoreUpdate />} />
    <Route path=":id">
      <Route index element={<CreditScoreDetail />} />
      <Route path="edit" element={<CreditScoreUpdate />} />
      <Route path="delete" element={<CreditScoreDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CreditScoreRoutes;
