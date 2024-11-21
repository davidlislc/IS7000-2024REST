import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IBatch {
  id?: number;
  name?: string;
  job?: string;
  rundate?: dayjs.Dayjs;
  batchstatus?: keyof typeof Status;
  user?: IUser | null;
}

export const defaultValue: Readonly<IBatch> = {};
