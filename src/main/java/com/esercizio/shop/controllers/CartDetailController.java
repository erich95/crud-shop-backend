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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value ="/cartDetail")
public class CartDetailController {

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /*@GetMapping(value = "/getCartDetailByUser")
    public CartDetail getCartDetailByUser(@RequestParam int idUser) {
        Optional<CartDetail> cartDetailOptional =  cartDetailRepository.findByCarts_User_Id(idUser);
        if (cartDetailOptional.isPresent()) {
            CartDetail cartDetail = cartDetailOptional.get();
            return cartDetail;
        }
        return null;
    }*/

    //Chiamare get Cart per ottenere il carrello completo, non questo servizio
    @GetMapping(value = "/getCartDetailByUser")
    public List<CartDetail> getCartDetailByUser(@RequestParam int idUser) {
        Optional<List<CartDetail>> cartDetailOptional =  cartDetailRepository.findAllByCarts_User_Id(idUser);
        if (cartDetailOptional.isPresent()) {
            List<CartDetail> cartDetail = cartDetailOptional.get();
            return cartDetail;
        }
        return null;
    }

    @PostMapping(value = "/addProductToCart")
    public Cart addProductToCart(@RequestParam int idUser, int idProduct, int quantityToAdd) {
        Optional<List<CartDetail>> cartDetailOptional =  cartDetailRepository.findAllByCarts_User_Id(idUser);
        Optional<Cart>  cartOptional = cartRepository.findByUser_Id(idUser);
        Optional<Product> optionalProduct = productRepository.findById(idProduct);

        Optional<Cart>  cartOptional2 = cartRepository.findByUser_IdAndCartDetail_Products_Id(idUser, idProduct);
        if (cartOptional2.isPresent()) {
            Cart cart = cartOptional2.get();
            cart.getCartDetail().get(0).setProductQuantity(cart.getCartDetail().get(0).getProductQuantity() + quantityToAdd);
            cart.getCartDetail().get(0).setTotalPrice(cart.getCartDetail().get(0).getProductQuantity() * cart.getCartDetail().get(0).getProducts().getPrice());
            cartRepository.save(cart);
            return cart;
        } else {
            cartRepository.findByUser_Id(idUser);
        }
        return null;
    }
}
