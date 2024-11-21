import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IService } from 'app/shared/model/service.model';

export interface ISubscriptions {
  id?: number;
  name?: string;
  subdate?: dayjs.Dayjs;
  status?: string;
  user?: IUser | null;
  service?: IService | null;
}

export const defaultValue: Readonly<ISubscriptions> = {};
