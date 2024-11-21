import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import InsyteLog from './insyte-log';
import InsyteLogDetail from './insyte-log-detail';
import InsyteLogUpdate from './insyte-log-update';
import InsyteLogDeleteDialog from './insyte-log-delete-dialog';

const InsyteLogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<InsyteLog />} />
    <Route path="new" element={<InsyteLogUpdate />} />
    <Route path=":id">
      <Route index element={<InsyteLogDetail />} />
      <Route path="edit" element={<InsyteLogUpdate />} />
      <Route path="delete" element={<InsyteLogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InsyteLogRoutes;
