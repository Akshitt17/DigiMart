import React, { useState, useEffect } from 'react';
import { getCustomerAddresses, addCustomerAddress } from '../api/customerService';
import { Row, Col, Card, Spinner, Alert, ListGroup, Badge } from 'react-bootstrap';
import AddressForm from './AddressForm'; // Assuming AddressForm is in the same components folder

const AddressManagement = ({ customer }) => {
    const [addresses, setAddresses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [formError, setFormError] = useState('');
    const [formLoading, setFormLoading] = useState(false);

    const fetchAddresses = async () => {
        if (!customer?.customerId) return;
        setLoading(true);
        try {
            const data = await getCustomerAddresses(customer.customerId);
            setAddresses(data);
        } catch (err) {
            setError(err.data?.message || 'Failed to load addresses.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchAddresses();
    }, [customer]);

    const handleAddAddress = async (addressData) => {
        setFormLoading(true);
        setFormError('');
        try {
            await addCustomerAddress(customer.customerId, addressData);
            // Refresh the address list on success
            fetchAddresses();
        } catch (err) {
            setFormError(err.data?.message || 'Failed to add address.');
        } finally {
            setFormLoading(false);
        }
    };

    return (
        <Row>
            <Col md={7}>
                <Card>
                    <Card.Header as="h5">My Delivery Addresses</Card.Header>
                    <Card.Body>
                        {loading && <div className="text-center"><Spinner animation="border" /></div>}
                        {error && <Alert variant="danger">{error}</Alert>}
                        {!loading && !error && (
                            <ListGroup variant="flush">
                                {addresses.length > 0 ? (
                                    addresses.map(addr => (
                                        <ListGroup.Item key={addr.addressId} className="d-flex justify-content-between align-items-start">
                                            <div>
                                                <strong>{addr.addressType}</strong>
                                                <p className="mb-0">{addr.streetAddress}, {addr.city}</p>
                                                <p className="mb-0 text-muted">{addr.state}, {addr.postalCode}</p>
                                            </div>
                                            {addr.isDefault && <Badge bg="success" pill>Default</Badge>}
                                        </ListGroup.Item>
                                    ))
                                ) : (
                                    <p>You have no saved addresses.</p>
                                )}
                            </ListGroup>
                        )}
                    </Card.Body>
                </Card>
            </Col>
            <Col md={5}>
                <Card>
                    <Card.Header as="h5">Add New Address</Card.Header>
                    <Card.Body>
                        {formError && <Alert variant="danger">{formError}</Alert>}
                        <AddressForm onSubmit={handleAddAddress} isLoading={formLoading} />
                    </Card.Body>
                </Card>
            </Col>
        </Row>
    );
};

export default AddressManagement;
