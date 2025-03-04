import { useState } from 'react'
import DashBoard from './Components/DashBoard'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import CreateContent from './Components/createContent';
import PaginatedBlog from './Components/PaginatedBlog';
import PublisherLogin from './Login/PublisherLogin';
import AdminDashBoard from './ADMIN/AdminDashBoard';
import AdminLogin from './ADMIN/AdminLogin';
import GetAllPublisher from './ADMIN/GetAllPublisher';
import CreatePublisher from './ADMIN/CreatePublisher';
import AllBlogs from './Public/AllBlogs';
import CreateUser from './Public/CreateUser';
import LoginUser from './Public/LoginUser';
function App() {
  const mockData = Array.from({ length: 30 }, (_, i) => ({
    title: `Blog Post ${i + 1}`,
    description: `This is the description for Blog Post ${i + 1}.`,
    image: 'https://via.placeholder.com/300',
  }));  
  const [count, setCount] = useState(0)

  return (
    <Router>
      <Routes>
        <Route path="/" element={<DashBoard />}>
        <Route path="all-content" element={<PaginatedBlog data={mockData} itemsPerPage={6} />} />
        <Route path="create-content" element={<CreateContent/>}/>
      
        </Route>
        <Route path="login-publisher" element={<PublisherLogin/>}/>
        <Route path="login-Admin" element={<AdminLogin/>}/>

        <Route path="admin" element={<AdminDashBoard />}>
         <Route path="GetAllPublisher" element={<GetAllPublisher />} />
         <Route path="CreatePublisher" element={<CreatePublisher/>}/>
        </Route>

        <Route/>
        <Route path="allBlogs" element ={<AllBlogs/>}/>
        <Route path="CreateUser" element ={<CreateUser/>}/>
        <Route path="LoginUser" element ={<LoginUser/>}/>
      </Routes>
    </Router>
  );
};

export default App