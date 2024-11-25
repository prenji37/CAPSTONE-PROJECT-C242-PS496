const { Sequelize } = require('sequelize');

const sequelize = new Sequelize('mindspace_db', 'root', '', {
  host: '34.87.19.0',
  dialect: 'mysql'
});

const connectDB = async () => {
  try {
    await sequelize.authenticate();
    console.log('MySQL connected');
  } catch (error) {
    console.error('Unable to connect to the database:', error);
  }
};

module.exports = { sequelize, connectDB };
