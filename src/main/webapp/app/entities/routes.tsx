import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Parents from './parents';
import Child from './child';
import Balance from './balance';
import Transactions from './transactions';
import Points from './points';
import Task from './task';
import Achivement from './achivement';
import CreditScore from './credit-score';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="parents/*" element={<Parents />} />
        <Route path="child/*" element={<Child />} />
        <Route path="balance/*" element={<Balance />} />
        <Route path="transactions/*" element={<Transactions />} />
        <Route path="points/*" element={<Points />} />
        <Route path="task/*" element={<Task />} />
        <Route path="achivement/*" element={<Achivement />} />
        <Route path="credit-score/*" element={<CreditScore />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
