import { createContext, ReactNode, useContext, useEffect, useMemo, useState } from 'react';
import { apiClient } from '../api/client';

export type UserProfile = {
  id: number;
  username: string;
  email: string;
  createdAt: string;
  updatedAt: string;
};

type AuthResult = {
  token: string;
  user: UserProfile;
};

type ApiResponse<T> = {
  success: boolean;
  message: string;
  data: T;
};

type AuthContextValue = {
  token: string | null;
  user: UserProfile | null;
  loading: boolean;
  login: (account: string, password: string) => Promise<void>;
  register: (username: string, email: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
};

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem('jobpilot_token'));
  const [user, setUser] = useState<UserProfile | null>(null);
  const [loading, setLoading] = useState(Boolean(token));

  useEffect(() => {
    if (!token) {
      setLoading(false);
      return;
    }

    apiClient
      .get<ApiResponse<UserProfile>>('/user/me')
      .then((response) => setUser(response.data.data))
      .catch(() => {
        localStorage.removeItem('jobpilot_token');
        setToken(null);
        setUser(null);
      })
      .finally(() => setLoading(false));
  }, [token]);

  const value = useMemo<AuthContextValue>(
    () => ({
      token,
      user,
      loading,
      async login(account, password) {
        const response = await apiClient.post<ApiResponse<AuthResult>>('/auth/login', { account, password });
        localStorage.setItem('jobpilot_token', response.data.data.token);
        setToken(response.data.data.token);
        setUser(response.data.data.user);
      },
      async register(username, email, password) {
        const response = await apiClient.post<ApiResponse<AuthResult>>('/auth/register', {
          username,
          email,
          password,
        });
        localStorage.setItem('jobpilot_token', response.data.data.token);
        setToken(response.data.data.token);
        setUser(response.data.data.user);
      },
      async logout() {
        try {
          await apiClient.post('/auth/logout');
        } finally {
          localStorage.removeItem('jobpilot_token');
          setToken(null);
          setUser(null);
        }
      },
    }),
    [loading, token, user],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used inside AuthProvider');
  }
  return context;
}
