Controller
```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController{
    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(
        @PathVariable Long id){
            return ResponseEntity.ok(
                employeeService.getEmployee(id));
        }
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(
        @RequestBody EmployeeDto employeeDto){
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.create(employeeDto));
        }
}
```

```