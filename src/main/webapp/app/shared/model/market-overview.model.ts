import dayjs from 'dayjs';
import { INDEX } from 'app/shared/model/enumerations/index.model';

export interface IMarketOverview {
  id?: number;
  name?: string;
  price?: number;
  change?: number;
  ticker?: keyof typeof INDEX;
  marketdate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IMarketOverview> = {};
