import dayjs from 'dayjs';
import { SECTOR } from 'app/shared/model/enumerations/sector.model';

export interface IMarketSector {
  id?: number;
  name?: keyof typeof SECTOR;
  price?: number;
  change?: number;
  marketdate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IMarketSector> = {};
