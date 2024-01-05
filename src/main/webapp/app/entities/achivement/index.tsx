import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Achivement from './achivement';
import AchivementDetail from './achivement-detail';
import AchivementUpdate from './achivement-update';
import AchivementDeleteDialog from './achivement-delete-dialog';

const AchivementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Achivement />} />
    <Route path="new" element={<AchivementUpdate />} />
    <Route path=":id">
      <Route index element={<AchivementDetail />} />
      <Route path="edit" element={<AchivementUpdate />} />
      <Route path="delete" element={<AchivementDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AchivementRoutes;
