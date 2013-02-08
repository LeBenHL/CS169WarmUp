require 'test_helper'

class UserTest < ActiveSupport::TestCase
  # test "the truth" do
  #   assert true
  # end
  test "Add a User" do
    response = User.add("Ben", "BenPassword")
    assert_equal(1, response)
    assert_not_nil(User.where("user = ?", "Ben"))
  end

  test "Add Multiple Users" do
    response = User.add("Ben", "BenPassword")
    assert_equal(1, response)
    assert_not_nil(User.where("user = ?", "Ben"))
    response = User.add("Ben2", "BenPassword")
    assert_equal(1, response)
    assert_not_nil(User.where("user = ?", "Ben2"))
  end

  test "Add a User w/o password" do
    response = User.add("Ben", "")
    assert_equal(1, response)
    assert_not_nil(User.where("user = ?", "Ben"))
  end

  test "Add a User w/ blank username" do
    response = User.add("", "BenPassword")
    assert_equal(-3, response)
  end

  test "Add a User w/ username 128 char long" do
    username = "H" * 128
    response = User.add(username, "BenPassword")
    assert_equal(1, response)
    assert_not_nil(User.where("user = ?", username))
  end

  test "Add a User w/ username too long" do
    username = "H" * 129
    response = User.add(username, "BenPassword")
    assert_equal(-3, response)
  end

  test "Add a Duplicate user" do
    response = User.add("Ben", "BenPassword")
    response = User.add("Ben", "BenPassword")
    assert_equal(-2, response)
  end

  test "Add a User w/ password 128 char long" do
    password = "H" * 128
    response = User.add("Ben", password)
    assert_equal(1, response)
    assert_not_nil(User.where("user = ?", "Ben"))
  end

  test "Add a User w/ password too long" do
    password = "H" * 129
    response = User.add("Ben", password)
    assert_equal(-4, response)
  end

  test "Login w/ one user once" do
    User.add("Ben", "BenPassword")
    response = User.login("Ben", "BenPassword")
    assert_equal(2, response)
  end

  test "Login w/ one user multiple times" do
    User.add("Ben", "BenPassword")
    response = User.login("Ben", "BenPassword")
    assert_equal(2, response)
    response = User.login("Ben", "BenPassword")
    assert_equal(3, response)
    response = User.login("Ben", "BenPassword")
    assert_equal(4, response)
  end

  test "Login w/ multiple users multiple times" do
    User.add("Ben", "BenPassword")
    User.add("Ben2", "Ben2Password")
    response = User.login("Ben", "BenPassword")
    assert_equal(2, response)
    response = User.login("Ben2", "Ben2Password")
    assert_equal(2, response)
    response = User.login("Ben", "BenPassword")
    assert_equal(3, response)
    response = User.login("Ben2", "Ben2Password")
    assert_equal(3, response)
    response = User.login("Ben", "BenPassword")
    assert_equal(4, response)
    response = User.login("Ben2", "Ben2Password")
    assert_equal(4, response)
  end

  test "Login w/ wrong password" do
    User.add("Ben", "BenPassword")
    response = User.login("Ben", "WrongPassword")
    assert_equal(-1, response)
  end

  test "Login w/ wrong user" do
    User.add("Ben", "BenPassword")
    response = User.login("WrongUser", "BenPassword")
    assert_equal(-1, response)
  end

  test "ResetFixtures" do
    User.add("Ben", "BenPassword")
    User.add("Ben2", "Ben2Password")
    User.TESTAPI_resetFixture
    assert_equal(0, User.find(:all).count)
  end
end
