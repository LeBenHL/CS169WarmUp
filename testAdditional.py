"""
Each file that starts with test... in this directory is scanned for subclasses of unittest.TestCase or testLib.RestTestCase
"""

import unittest
import os
import testLib

class TestAddUserWithoutUsername(testLib.RestTestCase):
    """Test adding users without username"""
    def assertResponse(self, respData, count = 1, errCode = testLib.RestTestCase.SUCCESS):
        """
        Check that the response data dictionary matches the expected values
        """
        expected = { 'errCode' : errCode }
        if count is not None:
            expected['count']  = count
        self.assertDictEqual(expected, respData)

    def testAdd2(self):
        respData = self.makeRequest("/users/add", method="POST", data = { 'user' : '', 'password' : 'password'} )
        self.assertResponse(respData, count = None, errCode = -3)

class TestAddUserWithoutPassword(testLib.RestTestCase):
    """Test adding users without password"""
    def assertResponse(self, respData, count = 1, errCode = testLib.RestTestCase.SUCCESS):
        """
        Check that the response data dictionary matches the expected values
        """
        expected = { 'errCode' : errCode }
        if count is not None:
            expected['count']  = count
        self.assertDictEqual(expected, respData)

    def testAdd3(self):
        respData = self.makeRequest("/users/add", method="POST", data = { 'user' : 'user', 'password' : ''} )
        self.assertResponse(respData, count = 1)


class TestAddUserWithMaxLengthUsername(testLib.RestTestCase):
    """Test adding users with 128 char username"""
    def assertResponse(self, respData, count = 1, errCode = testLib.RestTestCase.SUCCESS):
        """
        Check that the response data dictionary matches the expected values
        """
        expected = { 'errCode' : errCode }
        if count is not None:
            expected['count']  = count
        self.assertDictEqual(expected, respData)

    def testAdd4(self):
        respData = self.makeRequest("/users/add", method="POST", data = { 'user' : 'U' * 128, 'password' : 'password'} )
        self.assertResponse(respData, count = 1)

class TestAddUserWithMaxLengthPassword(testLib.RestTestCase):
    """Test adding users with 128 char password"""
    def assertResponse(self, respData, count = 1, errCode = testLib.RestTestCase.SUCCESS):
        """
        Check that the response data dictionary matches the expected values
        """
        expected = { 'errCode' : errCode }
        if count is not None:
            expected['count']  = count
        self.assertDictEqual(expected, respData)

    def testAdd5(self):
        respData = self.makeRequest("/users/add", method="POST", data = { 'user' : 'user', 'password' : 'p' * 128} )
        self.assertResponse(respData, count = 1)

class TestAddUserWithTooLongUsername(testLib.RestTestCase):
    """Test adding users with 129 char username"""
    def assertResponse(self, respData, count = 1, errCode = testLib.RestTestCase.SUCCESS):
        """
        Check that the response data dictionary matches the expected values
        """
        expected = { 'errCode' : errCode }
        if count is not None:
            expected['count']  = count
        self.assertDictEqual(expected, respData)

    def testAdd6(self):
        respData = self.makeRequest("/users/add", method="POST", data = { 'user' : 'U' * 129, 'password' : 'password'} )
        self.assertResponse(respData, count = None, errCode = -3)

class TestAddUserWithTooLongPassword(testLib.RestTestCase):
    """Test adding users with 129 char password"""
    def assertResponse(self, respData, count = 1, errCode = testLib.RestTestCase.SUCCESS):
        """
        Check that the response data dictionary matches the expected values
        """
        expected = { 'errCode' : errCode }
        if count is not None:
            expected['count']  = count
        self.assertDictEqual(expected, respData)

    def testAdd7(self):
        respData = self.makeRequest("/users/add", method="POST", data = { 'user' : 'user', 'password' : 'p' * 129} )
        self.assertResponse(respData, count = None, errCode = -4)

class TestLoginUser(testLib.RestTestCase):
    """Test login with a single user"""
    def assertResponse(self, respData, count = 1, errCode = testLib.RestTestCase.SUCCESS):
        """
        Check that the response data dictionary matches the expected values
        """
        expected = { 'errCode' : errCode }
        if count is not None:
            expected['count']  = count
        self.assertDictEqual(expected, respData)

    def testlogin1(self):
        self.makeRequest("/users/add", method="POST", data = { 'user' : 'user1', 'password' : 'password'} )
        respData = self.makeRequest("/users/login", method="POST", data = { 'user' : 'user1', 'password' : 'password'} )
        self.assertResponse(respData, count = 2)
        respData = self.makeRequest("/users/login", method="POST", data = { 'user' : 'user1', 'password' : 'password'} )
        self.assertResponse(respData, count = 3)

class TestLoginUserWithWrongPassword(testLib.RestTestCase):
    """Test login with wrong password"""
    def assertResponse(self, respData, count = 1, errCode = testLib.RestTestCase.SUCCESS):
        """
        Check that the response data dictionary matches the expected values
        """
        expected = { 'errCode' : errCode }
        if count is not None:
            expected['count']  = count
        self.assertDictEqual(expected, respData)

    def testlogin2(self):
        self.makeRequest("/users/add", method="POST", data = { 'user' : 'user1', 'password' : 'password'} )
        respData = self.makeRequest("/users/login", method="POST", data = { 'user' : 'user1', 'password' : 'thisIsNotMyPassword'} )
        self.assertResponse(respData, count = None, errCode = -1)

class TestLoginUserWithWrongUsername(testLib.RestTestCase):
    """Test login with wrong username"""
    def assertResponse(self, respData, count = 1, errCode = testLib.RestTestCase.SUCCESS):
        """
        Check that the response data dictionary matches the expected values
        """
        expected = { 'errCode' : errCode }
        if count is not None:
            expected['count']  = count
        self.assertDictEqual(expected, respData)

    def testlogin2(self):
        self.makeRequest("/users/add", method="POST", data = { 'user' : 'user1', 'password' : 'password'} )
        respData = self.makeRequest("/users/login", method="POST", data = { 'user' : 'thisIsNotUser1', 'password' : 'password'} )
        self.assertResponse(respData, count = None, errCode = -1)


    
