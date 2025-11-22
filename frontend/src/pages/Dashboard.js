import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Spinner } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { issueAPI } from '../services/api';

const Dashboard = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [recentIssues, setRecentIssues] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const [statsResponse, issuesResponse] = await Promise.all([
        issueAPI.getIssueStats(),
        issueAPI.getMyIssues()
      ]);
      
      setStats(statsResponse.data);
      setRecentIssues(issuesResponse.data.slice(0, 5)); // Show only recent 5 issues
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadgeClass = (status) => {
    switch (status) {
      case 'PENDING': return 'status-pending';
      case 'IN_PROGRESS': return 'status-in-progress';
      case 'RESOLVED': return 'status-resolved';
      case 'REJECTED': return 'status-rejected';
      default: return 'status-pending';
    }
  };

  if (loading) {
    return (
      <Container className="py-5">
        <div className="loading-spinner">
          <Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
          </Spinner>
        </div>
      </Container>
    );
  }

  return (
    <Container className="py-4">
      <Row className="mb-4">
        <Col>
          <h1>Welcome back, {user?.fullName}!</h1>
          <p className="text-muted">Here's an overview of your reported issues</p>
        </Col>
      </Row>

      {stats && (
        <Row className="mb-4">
          <Col md={3} className="mb-3">
            <Card className="stat-card">
              <div className="stat-number">{stats.pending || 0}</div>
              <div className="stat-label">Pending</div>
            </Card>
          </Col>
          <Col md={3} className="mb-3">
            <Card className="stat-card">
              <div className="stat-number">{stats.inProgress || 0}</div>
              <div className="stat-label">In Progress</div>
            </Card>
          </Col>
          <Col md={3} className="mb-3">
            <Card className="stat-card">
              <div className="stat-number">{stats.resolved || 0}</div>
              <div className="stat-label">Resolved</div>
            </Card>
          </Col>
          <Col md={3} className="mb-3">
            <Card className="stat-card">
              <div className="stat-number">{stats.rejected || 0}</div>
              <div className="stat-label">Rejected</div>
            </Card>
          </Col>
        </Row>
      )}

      <Row>
        <Col md={8}>
          <Card>
            <Card.Header className="d-flex justify-content-between align-items-center">
              <h5 className="mb-0">Recent Issues</h5>
              <Button as={Link} to="/my-issues" variant="outline-primary" size="sm">
                View All
              </Button>
            </Card.Header>
            <Card.Body>
              {recentIssues.length === 0 ? (
                <div className="text-center py-4">
                  <p className="text-muted">No issues reported yet.</p>
                  <Button as={Link} to="/report-issue" variant="primary">
                    Report Your First Issue
                  </Button>
                </div>
              ) : (
                <div className="list-group list-group-flush">
                  {recentIssues.map((issue) => (
                    <div key={issue.id} className="list-group-item">
                      <div className="d-flex justify-content-between align-items-start">
                        <div>
                          <h6 className="mb-1">{issue.issueType}</h6>
                          <p className="mb-1 text-muted">{issue.description}</p>
                          <small className="text-muted">
                            {new Date(issue.createdAt).toLocaleDateString()}
                          </small>
                        </div>
                        <div>
                          <span className={`status-badge ${getStatusBadgeClass(issue.status)}`}>
                            {issue.status.replace('_', ' ')}
                          </span>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </Card.Body>
          </Card>
        </Col>

        <Col md={4}>
          <Card>
            <Card.Header>
              <h5 className="mb-0">Quick Actions</h5>
            </Card.Header>
            <Card.Body>
              <div className="d-grid gap-2">
                <Button as={Link} to="/report-issue" variant="primary">
                  Report New Issue
                </Button>
                <Button as={Link} to="/my-issues" variant="outline-primary">
                  View My Issues
                </Button>
                {user?.role === 'ADMIN' && (
                  <Button as={Link} to="/admin" variant="outline-secondary">
                    Admin Dashboard
                  </Button>
                )}
              </div>
            </Card.Body>
          </Card>

          <Card className="mt-3">
            <Card.Header>
              <h5 className="mb-0">Issue Types</h5>
            </Card.Header>
            <Card.Body>
              <ul className="list-unstyled mb-0">
                <li className="mb-2">
                  <i className="fas fa-road text-warning me-2"></i>
                  Potholes
                </li>
                <li className="mb-2">
                  <i className="fas fa-lightbulb text-warning me-2"></i>
                  Broken Streetlights
                </li>
                <li className="mb-2">
                  <i className="fas fa-trash text-danger me-2"></i>
                  Garbage Issues
                </li>
                <li className="mb-2">
                  <i className="fas fa-tint text-info me-2"></i>
                  Water Stagnation
                </li>
              </ul>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Dashboard;
