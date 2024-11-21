import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MarketOverview from './market-overview';
import MarketOverviewDetail from './market-overview-detail';
import MarketOverviewUpdate from './market-overview-update';
import MarketOverviewDeleteDialog from './market-overview-delete-dialog';

const MarketOverviewRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MarketOverview />} />
    <Route path="new" element={<MarketOverviewUpdate />} />
    <Route path=":id">
      <Route index element={<MarketOverviewDetail />} />
      <Route path="edit" element={<MarketOverviewUpdate />} />
      <Route path="delete" element={<MarketOverviewDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MarketOverviewRoutes;
