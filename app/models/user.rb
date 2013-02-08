class User < ActiveRecord::Base
  attr_accessible :count, :password, :user
  validates :user, :presence => true, :uniqueness => true, :length => {:maximum => 128}
  validates :password, :length => {:maximum => 128}
  validates :count, :numericality => { :greater_than_or_equal_to => 0 }

  SUCCESS = 1
  ERR_BAD_CREDENTIALS = -1
  ERR_USER_EXISTS = -2
  ERR_BAD_USER_NAME = -3
  ERR_BAD_PASSWORD = -4

  def self.login(user, password)
    @user = User.where("user = ?", user)
    if @user
      return ERR_BAD_CREDENTIALS unless @user.password == password
      @user.count += 1
      @user.save
      return @user.count
    else
      return ERR_BAD_CREDENTIALS
    end
  end

  def self.add(user, password)
    begin
      @user = User.create!(:user => user, :password => password, :count => 1)
    rescue => ex
      return ex.message
      case ex.message
        when "Validation failed: User can't be blank"
          return ERR_BAD_USER_NAME
        when "Validation failed: User is too long (maximum is 128 characters)"
          return ERR_BAD_USER_NAME
        when "Validation failed: User has already been taken"
          return ERR_USER_EXISTS
        when "Validation failed: Password is too long (maximum is 128 characters)"
          return ERR_BAD_PASSWORD
        return
      end
    end
    return @user.count
  end

  def self.TESTAPI_resetFixture
    User.delete_all
    return SUCCESS
  end


end
