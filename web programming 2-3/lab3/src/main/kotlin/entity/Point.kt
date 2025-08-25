package entity

import jakarta.persistence.*

@Entity
@Table(name = "points")
open class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    var x: Double = 0.0
    var y: Double = 0.0
    var r: Int = 0

    @Column(name = "in_area")
    var inArea: Boolean = false

    constructor()

    constructor(x: Double, y: Double, r: Int, inArea: Boolean) {
        this.x = x
        this.y = y
        this.r = r
        this.inArea = inArea
    }
    constructor(id: Long, x: Double, y: Double, r: Int, inArea: Boolean) {
        this.id = id
        this.x = x
        this.y = y
        this.r = r
        this.inArea = inArea
    }
}