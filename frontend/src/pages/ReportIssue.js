import React, { useState } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert, Image } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { issueAPI } from '../services/api';
import { toast } from 'react-toastify';

const ReportIssue = () => {
  const [formData, setFormData] = useState({
    issueType: '',
    description: '',
    location: ''
  });
  const [photo, setPhoto] = useState(null);
  const [photoPreview, setPhotoPreview] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const navigate = useNavigate();

  const issueTypes = [
    { value: 'POTHOLE', label: 'Pothole' },
    { value: 'BROKEN_STREETLIGHT', label: 'Broken Streetlight' },
    { value: 'GARBAGE', label: 'Garbage' },
    { value: 'WATER_STAGNATION', label: 'Water Stagnation' }
  ];

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handlePhotoChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setPhoto(file);
      
      // Create preview
      const reader = new FileReader();
      reader.onload = (e) => {
        setPhotoPreview(e.target.result);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      await issueAPI.createIssue(formData, photo);
      toast.success('Issue reported successfully!');
      navigate('/my-issues');
    } catch (error) {
      const errorMessage = error.response?.data?.error || 'Failed to report issue';
      setError(errorMessage);
      toast.error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container className="py-4">
      <Row className="justify-content-center">
        <Col md={8} lg={6}>
          <Card className="shadow">
            <Card.Header className="text-center">
              <h3>Report a Public Service Issue</h3>
              <p className="text-muted mb-0">Help improve your community</p>
            </Card.Header>
            <Card.Body className="p-4">
              {error && (
                <Alert variant="danger" className="mb-3">
                  {error}
                </Alert>
              )}

              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label>Issue Type *</Form.Label>
                  <Form.Select
                    name="issueType"
                    value={formData.issueType}
                    onChange={handleChange}
                    required
                  >
                    <option value="">Select an issue type</option>
                    {issueTypes.map((type) => (
                      <option key={type.value} value={type.value}>
                        {type.label}
                      </option>
                    ))}
                  </Form.Select>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Location *</Form.Label>
                  <Form.Control
                    type="text"
                    name="location"
                    value={formData.location}
                    onChange={handleChange}
                    required
                    placeholder="Enter the exact location (e.g., Park Avenue, near Main Street)"
                  />
                  <Form.Text className="text-muted">
                    Be as specific as possible to help us locate the issue quickly.
                  </Form.Text>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Description *</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={4}
                    name="description"
                    value={formData.description}
                    onChange={handleChange}
                    required
                    placeholder="Describe the issue in detail..."
                  />
                  <Form.Text className="text-muted">
                    Provide as much detail as possible about the issue.
                  </Form.Text>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Photo (Optional)</Form.Label>
                  <Form.Control
                    type="file"
                    accept="image/*"
                    onChange={handlePhotoChange}
                  />
                  <Form.Text className="text-muted">
                    Upload a photo to help us understand the issue better.
                  </Form.Text>
                </Form.Group>

                {photoPreview && (
                  <div className="mb-3">
                    <Form.Label>Photo Preview</Form.Label>
                    <div>
                      <Image
                        src={photoPreview}
                        alt="Preview"
                        className="photo-preview"
                        rounded
                      />
                    </div>
                  </div>
                )}

                <div className="d-grid gap-2">
                  <Button
                    type="submit"
                    variant="primary"
                    size="lg"
                    disabled={loading}
                  >
                    {loading ? 'Reporting Issue...' : 'Report Issue'}
                  </Button>
                  <Button
                    type="button"
                    variant="outline-secondary"
                    onClick={() => navigate('/dashboard')}
                  >
                    Cancel
                  </Button>
                </div>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default ReportIssue;
