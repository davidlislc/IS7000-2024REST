import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface ITransaction {
  id?: number;
  name?: string;
  transdate?: dayjs.Dayjs;
  type?: string;
  amount?: number;
  user?: IUser | null;
}

export const defaultValue: Readonly<ITransaction> = {};
