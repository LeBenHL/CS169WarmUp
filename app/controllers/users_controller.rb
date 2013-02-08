class UsersController < ApplicationController

  respond_to :json, :html

  # POST /users/login
  def login
    response = User.login(params["user"], params["password"])
    hash = {}
    if response > 0
      hash[:errCode] = 1
      hash[:count] = response
    else
      hash[:errCode] = response
    end
    render :json => hash
  end

  # POST /users/add
  def add
    response = User.add(params["user"], params["password"])
    @hash = {}
    if response > 0
      @hash[:errCode] = 1
      @hash[:count] = response
    else
      @hash[:errCode] = response
    end
    render :json => @hash
  end

  # POST /TESTAPI/resetFixture
  def resetFixture
    response = User.TESTAPI_resetFixture
    @hash = {}
    @hash[:errCode] = response
    render :json => @hash
  end

  def unitTests
    resetTestDb = system 'rake db:test:prepare'
    response = `ruby -Itest test/unit/user_test.rb`
    parseResponse = response.scan(/[\s\S]+Finished tests.*([\s\S]+)\n([0-9]+) tests, ([0-9]+) assertions, ([0-9]+) failures, ([0-9]+) errors, ([0-9]+) skips/)
    totalTests = parseResponse[1]
    nrFailed = parseResponse[3]
    output = parseResponse[1]
    @hash = {}
    @hash[:totalTests] = totalTests
    @hash[:nrFailed] = nrFailed
    @hash[:output] = output
    render :json => @hash
  end

end
