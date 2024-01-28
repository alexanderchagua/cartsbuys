import java.io.File  // Import the File class for file handling
import java.util.*  // Import the Scanner class and other Java utilities

// Define a data class Product to represent a product with name and price
data class Product(val name: String, val price: Double)

// Define a class ShoppingCart to represent a shopping cart
class ShoppingCart {
    // Mutable map to store products in the cart along with their quantity
    private val cartItems = mutableMapOf<Product, Int>()

    // Method to add a product to the cart
    fun addProduct(product: Product, quantity: Int) {
        if (cartItems.containsKey(product)) {
            cartItems[product] = cartItems[product]!! + quantity
        } else {
            cartItems[product] = quantity
        }
        println("$quantity ${product.name}(s) added to the cart.")
    }

    // Method to view the contents of the cart
    fun viewCart() {
        println("------ Shopping Cart ------")
        for ((product, quantity) in cartItems) {
            println("${product.name} - Quantity: $quantity - Price: $${product.price * quantity}")
        }
        println("---------------------------")
        println("Total Price: $${calculateTotalPrice()}")
    }

    // Private method to calculate the total price of the cart
    private fun calculateTotalPrice(): Double {
        return cartItems.entries.sumByDouble { it.key.price * it.value }
    }

    // Method to process payment and clear the cart
    fun checkout(paymentMethod: String) {
        val totalAmount = calculateTotalPrice()
        println("Total Amount: $$totalAmount")

        when (paymentMethod.toLowerCase(Locale.ROOT)) {
            "cash" -> {  // If payment method is cash
                println("Payment received in cash. Thank you for your purchase!")
                clearCart()  // Clear the cart
            }
            else -> println("Invalid payment method. Please choose 'cash' for cash payment.")
        }
    }

    // Private method to clear the cart
    private fun clearCart() {
        cartItems.clear()
        println("Shopping cart cleared.")
    }

    // Method to save the contents of the cart to a text file
    fun saveCartToFile(filename: String) {
        val file = File(filename)
        file.bufferedWriter().use { out ->
            for ((product, quantity) in cartItems) {
                out.write("${product.name},${product.price},${quantity}\n")
            }
        }
        println("Shopping cart saved to $filename")
    }

    // Method to load the contents of the cart from a text file
    fun loadCartFromFile(filename: String) {
        val file = File(filename)
        if (file.exists()) {
            file.bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    val parts = line.split(",")
                    val name = parts[0]
                    val price = parts[1].toDouble()
                    val quantity = parts[2].toInt()
                    val product = Product(name, price)
                    cartItems[product] = quantity
                }
            }
            println("Shopping cart loaded from $filename")
        } else {
            println("File $filename not found. No items loaded.")
        }
    }
}

// Main function that runs the program
fun main() {
    // Create some example products
    val product1 = Product("Laptop", 800.0)
    val product2 = Product("Smartphone", 500.0)
    val product3 = Product("Headphones", 100.0)

    // Create a shopping cart
    val cart = ShoppingCart()
    val scanner = Scanner(System.`in`)  // Create a Scanner object for user input

    while (true) {
        // Show the menu options
        println("\nOptions:")
        println("1. Add Product to Cart")
        println("2. View Cart")
        println("3. Checkout")
        println("4. Save Cart to File")
        println("5. Load Cart from File")
        println("6. Exit")

        print("Select an option: ")
        when (scanner.nextInt()) {
            1 -> {
                // Add a product to the cart
                println("\nAvailable Products:")
                println("1. ${product1.name} - $${product1.price}")
                println("2. ${product2.name} - $${product2.price}")
                println("3. ${product3.name} - $${product3.price}")

                print("Select a product (1-3): ")
                val selectedProduct = when (scanner.nextInt()) {
                    1 -> product1
                    2 -> product2
                    3 -> product3
                    else -> {
                        println("Invalid selection. Returning to main menu.")
                        continue
                    }
                }

                print("Enter quantity: ")
                val quantity = scanner.nextInt()
                cart.addProduct(selectedProduct, quantity)
            }
            2 -> cart.viewCart()  // View the contents of the cart
            3 -> {
                // Process payment
                print("\nSelect payment method (cash): ")
                val paymentMethod = scanner.next()
                cart.checkout(paymentMethod)
            }
            4 -> {
                // Save the contents of the cart to a file
                print("Enter filename to save cart: ")
                val filename = scanner.next()
                cart.saveCartToFile(filename)
            }
            5 -> {
                // Load the contents of the cart from a file
                print("Enter filename to load cart: ")
                val filename = scanner.next()
                cart.loadCartFromFile(filename)
            }
            6 -> {
                // Exit the program
                println("Exiting program.")
                break
            }
            else -> println("Invalid option. Please choose a valid option.")
        }
    }
}
