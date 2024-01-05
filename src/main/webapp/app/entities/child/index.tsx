import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Child from './child';
import ChildDetail from './child-detail';
import ChildUpdate from './child-update';
import ChildDeleteDialog from './child-delete-dialog';

const ChildRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Child />} />
    <Route path="new" element={<ChildUpdate />} />
    <Route path=":id">
      <Route index element={<ChildDetail />} />
      <Route path="edit" element={<ChildUpdate />} />
      <Route path="delete" element={<ChildDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ChildRoutes;
