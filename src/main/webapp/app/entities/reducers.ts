import subscriptions from 'app/entities/subscriptions/subscriptions.reducer';
import service from 'app/entities/service/service.reducer';
import transaction from 'app/entities/transaction/transaction.reducer';
import wallet from 'app/entities/wallet/wallet.reducer';
import batch from 'app/entities/batch/batch.reducer';
import insyteLog from 'app/entities/insyte-log/insyte-log.reducer';
import marketSector from 'app/entities/market-sector/market-sector.reducer';
import marketOverview from 'app/entities/market-overview/market-overview.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  subscriptions,
  service,
  transaction,
  wallet,
  batch,
  insyteLog,
  marketSector,
  marketOverview,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
