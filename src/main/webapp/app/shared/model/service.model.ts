export interface IService {
  id?: number;
  name?: string;
  level?: string;
  interval?: string;
  price?: number;
}

export const defaultValue: Readonly<IService> = {};
