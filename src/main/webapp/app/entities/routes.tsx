import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Subscriptions from './subscriptions';
import Service from './service';
import Transaction from './transaction';
import Wallet from './wallet';
import Batch from './batch';
import InsyteLog from './insyte-log';
import MarketSector from './market-sector';
import MarketOverview from './market-overview';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="subscriptions/*" element={<Subscriptions />} />
        <Route path="service/*" element={<Service />} />
        <Route path="transaction/*" element={<Transaction />} />
        <Route path="wallet/*" element={<Wallet />} />
        <Route path="batch/*" element={<Batch />} />
        <Route path="insyte-log/*" element={<InsyteLog />} />
        <Route path="market-sector/*" element={<MarketSector />} />
        <Route path="market-overview/*" element={<MarketOverview />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
