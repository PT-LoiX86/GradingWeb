import React from 'react';
import Layout from './Layout';
import Dashboard from './Dashboard';

interface HomeProps {
  onLogout: () => Promise<void>;
  onNavigate: (path: string) => void;
}

const Home: React.FC<HomeProps> = ({ onLogout, onNavigate }) => {
  return (
    <Layout onLogout={onLogout} onNavigate={onNavigate}>
      <Dashboard />
    </Layout>
  );
};

export default Home;
