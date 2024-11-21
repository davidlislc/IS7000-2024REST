import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MarketSector from './market-sector';
import MarketSectorDetail from './market-sector-detail';
import MarketSectorUpdate from './market-sector-update';
import MarketSectorDeleteDialog from './market-sector-delete-dialog';

const MarketSectorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MarketSector />} />
    <Route path="new" element={<MarketSectorUpdate />} />
    <Route path=":id">
      <Route index element={<MarketSectorDetail />} />
      <Route path="edit" element={<MarketSectorUpdate />} />
      <Route path="delete" element={<MarketSectorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MarketSectorRoutes;
