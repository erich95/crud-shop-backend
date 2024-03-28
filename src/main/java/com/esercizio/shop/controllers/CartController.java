package com.esercizio.shop.controllers;

import com.esercizio.shop.entities.Cart;
import com.esercizio.shop.entities.CartDetail;
import com.esercizio.shop.entities.Product;
import com.esercizio.shop.entities.User;
import com.esercizio.shop.repositories.CartDetailRepository;
import com.esercizio.shop.repositories.CartRepository;
import com.esercizio.shop.repositories.ProductRepository;
import com.esercizio.shop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/cart")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartDetailRepository cartDetailRepository;

    @GetMapping(value = "/getCart")
    public Cart getCart(@RequestParam int idUser) {
        Optional<Cart> cart = cartRepository.findByUser_Id(idUser);
        Cart cart1 = null;
        if (cart.isPresent()) {
            cart1 = cart.get();
        }
        return cart1;
    }

    @PostMapping(value="/clearCart")
    public Cart clearCart(@RequestParam int idUser) {
        Optional<Cart> cartOpt = cartRepository.findByUser_Id(idUser);
        List<Integer> listIdCartDetails = new ArrayList<>();
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            if (cart.getCartDetail().size() > 0) {
                for (int i = 0; i < cart.getCartDetail().size(); i++) {
                    listIdCartDetails.add(cart.getCartDetail().get(i).getId());
                }
                cart.getCartDetail().clear();
                cartRepository.save(cart);
                for (int i = 0; i < listIdCartDetails.size(); i++) {
                    cartDetailRepository.deleteById(listIdCartDetails.get(i));
                }
                return cart;
            }
        }
        return new Cart();
    }

    @PostMapping(value="/removeProductFromCart")
    public Cart removeProductFromCart(@RequestParam int idUser, int idProduct) {
        Optional<Cart> cartOpt = cartRepository.findByUser_Id(idUser);
        CartDetail cartDetailToRemove = new CartDetail();
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            if (cart.getCartDetail().size() > 0) {
                for (int i = 0; i < cart.getCartDetail().size(); i++) {
                    if (cart.getCartDetail().get(i).getProducts().getId() == idProduct) {
                        cart.getCartDetail().remove(cart.getCartDetail().get(i));
                        break;
                    }
                }
                cartRepository.save(cart);
            }
        }
        return null;
    }


    @PostMapping(value="/addToCart")
    public ResponseEntity<Map<String, String>> addProductToCart(@RequestParam int idUser, int idProduct, int productQuantity) {
        Map<String, String> response = new HashMap<>();
        Optional<Cart> cartOpt = cartRepository.findByUser_Id(idUser);
        Optional<Product> productOpt = productRepository.findById(idProduct);
        Optional<User> userOpt = userRepository.findById(idUser);
        Product product = new Product();
        User user = new User();
        List<CartDetail> cartDetailList = new ArrayList<>();
        if (productOpt.isPresent()) {
            product = productOpt.get();
        }
        if (userOpt.isPresent()) {
            user = userOpt.get();
        }
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            if (cart.getCartDetail() != null && cart.getCartDetail().size() > 0) {
                for (int i = 0; i < cart.getCartDetail().size(); i++) {
                    if (cart.getCartDetail().get(i).getProducts().getId() == idProduct) {
                        cart.getCartDetail().get(i).setProductQuantity(cart.getCartDetail().get(i).getProductQuantity() + productQuantity);
                        cart.getCartDetail().get(i).setTotalPrice(cart.getCartDetail().get(i).getProductQuantity() * cart.getCartDetail().get(i).getProducts().getPrice());
                        cartDetailRepository.save(cart.getCartDetail().get(i));
                        response.put("message", "The product has been added to your cart!");
                        return ResponseEntity.ok(response);
                    }
                }
                CartDetail cartDetail = new CartDetail();
                cartDetail.setProductQuantity(productQuantity);
                cartDetail.setProducts(product);
                cartDetail.setTotalPrice(product.getPrice() * cartDetail.getProductQuantity());
                cartDetailRepository.save(cartDetail);
                cart.getCartDetail().add(cartDetail);
                cartRepository.save(cart);
                response.put("message", "The product has been added to your cart!");
                return ResponseEntity.ok(response);
            } else {
                CartDetail cartDetail = new CartDetail();
                cartDetail.setProductQuantity(productQuantity);
                cartDetail.setProducts(product);
                cartDetail.setTotalPrice(product.getPrice() * cartDetail.getProductQuantity());
                cartDetailRepository.save(cartDetail);
                cart.getCartDetail().add(cartDetail);
                cartRepository.save(cart);
                response.put("message", "The product has been added to your cart!");
                return ResponseEntity.ok(response);
            }
        } else {
            Cart cart = new Cart();
            CartDetail cartDetail = new CartDetail();
            cartDetail.setProductQuantity(productQuantity);
            cartDetail.setProducts(product);
            cartDetail.setTotalPrice(product.getPrice() * cartDetail.getProductQuantity());
            cart.setUser(user);
            cart = cartRepository.save(cart);
            cartDetailRepository.save(cartDetail);
            cartDetailList.add(cartDetail);
            cart.setCartDetail(cartDetailList);
            cartRepository.save(cart);
            response.put("message", "The product has been added to your cart!");
            return ResponseEntity.ok(response);
        }
    }
}
