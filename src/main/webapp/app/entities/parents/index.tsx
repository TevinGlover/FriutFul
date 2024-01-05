import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Parents from './parents';
import ParentsDetail from './parents-detail';
import ParentsUpdate from './parents-update';
import ParentsDeleteDialog from './parents-delete-dialog';

const ParentsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Parents />} />
    <Route path="new" element={<ParentsUpdate />} />
    <Route path=":id">
      <Route index element={<ParentsDetail />} />
      <Route path="edit" element={<ParentsUpdate />} />
      <Route path="delete" element={<ParentsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ParentsRoutes;
