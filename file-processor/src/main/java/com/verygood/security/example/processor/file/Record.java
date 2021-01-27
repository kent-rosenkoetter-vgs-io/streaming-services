package com.verygood.security.example.processor.file;

import java.util.Objects;

public class Record {

  public static final Record COMPLETED = new Record(null, null, null, null, null, null, null, null, null, null) {};

  // Address,City,State,Zip,First Name,Last Name,Card Type,Card Num,Expiration,CVV
  private final String address;
  private final String city;
  private final String state;
  private final String zip;
  private final String firstName;
  private final String lastName;
  private final String cardType;
  private final String cardNum;
  private final String expiration;
  private final String cvv;

  public Record(final String address, final String city, final String state, final String zip,
      final String firstName, final String lastName, final String cardType, final String cardNum,
      final String expiration, final String cvv) {
    this.address = address;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.firstName = firstName;
    this.lastName = lastName;
    this.cardType = cardType;
    this.cardNum = cardNum;
    this.expiration = expiration;
    this.cvv = cvv;
  }

  public String getAddress() {
    return address;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public String getZip() {
    return zip;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getCardType() {
    return cardType;
  }

  public String getCardNum() {
    return cardNum;
  }

  public String getExpiration() {
    return expiration;
  }

  public String getCVV() {
    return cvv;
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, city, state, zip, firstName, lastName, cardType, cardNum, expiration, cvv);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Record record = (Record) o;
    return Objects.equals(address, record.address) && Objects.equals(city, record.city)
        && Objects.equals(state, record.state) && Objects.equals(zip, record.zip) && Objects
        .equals(firstName, record.firstName) && Objects.equals(lastName, record.lastName) && Objects
        .equals(cardType, record.cardType) && Objects.equals(cardNum, record.cardNum) && Objects
        .equals(expiration, record.expiration) && Objects.equals(cvv, record.cvv);
  }

  @Override
  public String toString() {
    return "Record{" + "address='" + address + '\'' + ", city='" + city + '\'' + ", state='" + state + '\'' + ", zip='" + zip + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", cardType='" + cardType + '\'' + ", cardNum='" + cardNum + '\'' + ", expiration='" + expiration + '\'' + ", cvv='" + cvv + '\'' + '}';
  }
}
