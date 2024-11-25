const express = require('express');
const { connectDB } = require('./mysql_db');
const User = require('./models/User');
const bcrypt = require('bcryptjs');
const app = express();
const port = 3000;

connectDB();
app.use(express.json());

// Endpoint Registrasi
app.post('/register', async (req, res) => {
  const { email, password } = req.body;

  try {
    let user = await User.findOne({ where: { email } });
    if (user) {
      return res.status(400).json({ message: 'User already exists' });
    }

    const hashedPassword = await bcrypt.hash(password, 10);
    user = await User.create({ email, password: hashedPassword });
    res.status(201).json({ message: 'User registered successfully' });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({ message: 'Server error' });
  }
});

// Endpoint Login
app.post('/login', async (req, res) => {
  const { email, password } = req.body;

  try {
    const user = await User.findOne({ where: { email } });
    if (!user) {
      return res.status(400).json({ message: 'Invalid credentials' });
    }

    const isMatch = await bcrypt.compare(password, user.password);
    if (!isMatch) {
      return res.status(400).json({ message: 'Invalid credentials' });
    }

    res.status(200).json({ message: 'Login successful' });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({ message: 'Server error' });
  }
});

app.get('/', async (req, res) => { 
    try { 
        const users = await User.findAll(); 
        res.status(200).json(users); 
    } catch (err) { 
        console.error(err.message); 
        res.status(500).json({ message: 'Server error' }); 
    } 
});

app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});
