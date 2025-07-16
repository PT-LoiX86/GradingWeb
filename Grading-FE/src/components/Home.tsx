import React from 'react';
import Layout from './Layout';
import Dashboard from './Dashboard';

interface HomeProps {
  onLogout: () => Promise<void>;
}

const Home: React.FC<HomeProps> = ({ onLogout }) => {
  return (
    <Layout onLogout={onLogout}>
      <Dashboard />
    </Layout>
  );
};

export default Home;
