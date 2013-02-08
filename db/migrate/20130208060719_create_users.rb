class CreateUsers < ActiveRecord::Migration
  def change
    create_table :users do |t|
      t.string :user, :limit => 128, :null => false
      t.string :password, :limit => 128, :null => false
      t.integer :count

      t.timestamps
    end
  end
end
