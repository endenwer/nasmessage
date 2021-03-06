"use strict";

var NasMessageContract = function () {
  LocalContractStorage.defineProperties(this, {
    message: null,
    sender: null,
    paidAmount: {
      stringify: function (obj) {
        return obj.toString();
      },
      parse: function (str) {
        return new BigNumber(str);
      }
    },
    amountStep: {
      stringify: function (obj) {
        return obj.toString();
      },
      parse: function (str) {
        return new BigNumber(str);
      }
    }
  });
};

NasMessageContract.prototype = {
  init: function () {
    this.amountStep = this._toWei(0.00001);
    this.paidAmount = new BigNumber(0);
  },

  _toWei: function(value) {
    var data = new BigNumber(value);
    var one_nas = new BigNumber(Math.pow(10, 18));
    return one_nas.times(data);
  },

  _toNas: function(value) {
    var data = new BigNumber(value);
    var one_nas = new BigNumber(Math.pow(10, 18));
    return data.dividedBy(one_nas);
  },

  _validateMessage: function(message) {
    if(!message || message == "") {
      throw new Error("No message provided");
    }

    if(message.length > 140) {
      throw new Error("Invalid message length, max 140 characters");
    }

    return true;
  },

  _validateAmount: function(amount) {
    if(amount.lessThan(this.paidAmount.plus(this.amountStep))) {
      throw new Error("Invalid payment amount");
    }

    return true;
  },

  getCurrentState: function () {
    return {
      message:     this.message,
      sender:      this.sender,
      paidAmount:  this._toNas(this.paidAmount),
      amountStep:  this._toNas(this.amountStep)
    };
  },

  setMessage: function(newMessage) {
    var message = this.message;

    if (newMessage) {
      newMessage = newMessage.trim();
    }

    this._validateMessage(newMessage);
    this._validateAmount(Blockchain.transaction.value);

    if(this.sender) {
      Blockchain.transfer(this.sender, this.paidAmount);
    }

    this.message = newMessage;
    this.sender = Blockchain.transaction.from;
    this.paidAmount = new BigNumber(Blockchain.transaction.value);

    return true;
  },

  returnFunds: function() {
    var sender = this.sender;

    if(!sender || sender != Blockchain.transaction.from) {
      throw new Error("You don't have funds to return");
    }

    var result = Blockchain.transfer(sender, this.paidAmount);

    if (!result) {
      throw new Error("Transfer failed");
    }

    this.sender = null;
    this.message = null;
    this.paidAmount = new BigNumber(0);

    return true;
  }
};

module.exports = NasMessageContract;
