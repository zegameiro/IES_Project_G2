import { useState, useEffect } from 'react';
import Navbar from '../layout/Navbar';

import { BASE_API_URL } from '../../constants';

import axios from 'axios';
import Header from '../layout/Header';
import DeviceCard from '../layout/DeviceCard';

import { useNavigate } from 'react-router-dom';

const DevicesPage = () => {
  const navigate = useNavigate();
  const [devices, setDevices] = useState([]);
  const [rooms, setRooms] = useState([]);
  const search = new URLSearchParams(window.location.search).get('search');

  useEffect(() => {
    if (localStorage.getItem('user')) {
      getDevices();
      getRooms();
    } else {
      navigate('/login?redirect=dashboard');
    }
  }, []);

    useEffect(() => {
        console.log('search changed -> ', search);
    }, [search]);

  const getDevices = async () => {
    try {
      const res = await axios.get(`${BASE_API_URL}/outputs/list`);
      if (res.status === 200) {
        console.log('received data');
        console.log('Devices -> ', res.data);
        setDevices(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const getRooms = async () => {
    try {
      const res = await axios.get(`${BASE_API_URL}/room/list`);
      if (res.status === 200) {
        console.log('received data');
        console.log('Rooms -> ', res.data);
        setRooms(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="mx-[5%] mt-4 flex justify-between">
      <Navbar />
      <div className="flex flex-col w-full h-full">
        <Header />
        <div>
          <h1 className="text-4xl font-bold m-4">Devices</h1>
          <div className="flex flex-wrap mx-4">
            {devices != [] ? (
              devices.filter((device) => {
                if (search === null) {
                  return device;
                } else if (
                  device?.name
                    .toLowerCase()
                    .includes(search.toLowerCase())
                ) {
                  return device;
                }
              }).map((device) => {
                return (
                  <span
                    className="m-2"
                    key={device?.uid}
                  >
                    <DeviceCard
                      device={device}
                      isBig
                      rooms={rooms}
                    />
                  </span>
                );
              })
            ) : (
              <h1 className="text-4xl font-bold m-4">No devices found</h1>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default DevicesPage;
