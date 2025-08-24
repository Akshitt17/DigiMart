import React, { createContext, useReducer, useContext, useEffect } from "react";
import toast from "react-hot-toast";

const initialState = {
  items: JSON.parse(localStorage.getItem("cartItems")) || [],
};

const cartReducer = (state, action) => {
  switch (action.type) {
    case "ADD_ITEM": {
      const newItem = action.payload;
      const existingItem = state.items.find(
        (item) => item.deviceId === newItem.deviceId
      );

      let updatedItems;
      if (existingItem) {
        // Update quantity if item exists
        updatedItems = state.items.map((item) =>
          item.deviceId === newItem.deviceId
            ? { ...item, quantity: item.quantity + 1 }
            : item
        );
      } else {
        // Add new item with quantity 1
        updatedItems = [...state.items, { ...newItem, quantity: 1 }];
      }
      return { ...state, items: updatedItems };
    }
    case "REMOVE_ITEM": {
      const itemIdToRemove = action.payload;
      const updatedItems = state.items.filter(
        (item) => item.deviceId !== itemIdToRemove
      );
      return { ...state, items: updatedItems };
    }
    case "UPDATE_QUANTITY": {
      const { deviceId, quantity } = action.payload;
      const updatedItems = state.items.map((item) =>
        item.deviceId === deviceId
          ? { ...item, quantity: Math.max(1, quantity) }
          : item
      );
      return { ...state, items: updatedItems };
    }
    case "CLEAR_CART":
      return { ...state, items: [] };
    default:
      return state;
  }
};

const CartContext = createContext();

export const CartProvider = ({ children }) => {
  const [state, dispatch] = useReducer(cartReducer, initialState);

  useEffect(() => {
    localStorage.setItem("cartItems", JSON.stringify(state.items));
  }, [state.items]);

  // Rename addItem to addToCart for consistency
  const addToCart = (item) => {
    dispatch({ type: "ADD_ITEM", payload: item });
    toast.success(`  Item added to cart successfully 🎉`, 4000);
  };
  const removeItem = (deviceId) => {
    dispatch({ type: "REMOVE_ITEM", payload: deviceId });
    toast.error(`  Item removed from cart successfully 🎉`, 4000);
  };

  const updateQuantity = (deviceId, quantity) =>
    dispatch({ type: "UPDATE_QUANTITY", payload: { deviceId, quantity } });

  const clearCart = () => {
    dispatch({ type: "CLEAR_CART" });
    toast.success(`  Cart cleared successfully 🎉`, 4000);
  };

  const cartCount = state.items.reduce(
    (total, item) => total + item.quantity,
    0
  );
  const cartTotal = state.items.reduce(
    (total, item) => total + item.price * item.quantity,
    0
  );

  const value = {
    items: state.items,
    addToCart, // Consistent name
    removeItem,
    updateQuantity,
    clearCart,
    cartCount,
    cartTotal,
  };

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
};

export const useCart = () => {
  return useContext(CartContext);
};
