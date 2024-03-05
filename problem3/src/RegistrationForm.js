import React, { useState } from 'react';

const RegistrationForm = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    npiNumber: '',
    businessAddress: '',
    telephoneNumber: '',
    email: '',
  });

  const [message, setMessage] = useState('');

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('https://www.availity.com/api/v1/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        setMessage('Registration successful');
        clearForm(); // Clear form after successful registration
      } else {
        setMessage('An error occurred during registration');
      }
    } catch (error) {
      setMessage('An error occurred during registration: ' + error.message); // Provide more detailed error message
    }
  };

  const clearForm = () => {
    setFormData({
      firstName: '',
      lastName: '',
      npiNumber: '',
      businessAddress: '',
      telephoneNumber: '',
      email: '',
    });
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <input name="firstName" placeholder="First Name" value={formData.firstName} onChange={handleChange} required />
        <input name="lastName" placeholder="Last Name" value={formData.lastName} onChange={handleChange} required />
        <input name="npiNumber" placeholder="NPI Number" value={formData.npiNumber} onChange={handleChange} required />
        <input name="businessAddress" placeholder="Business Address" value={formData.businessAddress} onChange={handleChange} required />
        <input name="telephoneNumber" placeholder="Telephone Number" value={formData.telephoneNumber} onChange={handleChange} required />
        <input name="email" type="email" placeholder="Email Address" value={formData.email} onChange={handleChange} required />
        <button type="submit">Register</button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
};

export default RegistrationForm;
